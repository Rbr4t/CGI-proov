<script lang="ts">
  import { onMount } from "svelte";
  import {
    getTables,
    getReservations,
    getRecommendations,
    getTablePairRecommendations,
  } from "./lib/api";
  import type { Table, Reservation } from "./lib/types";
  import FilterPanel from "./components/FilterPanel.svelte";
  import FloorPlan from "./components/FloorPlan.svelte";
  import TimeController from "./components/TimeController.svelte";
  import BookingModal from "./components/BookingModal.svelte";

  let tables: Table[] = $state([]);
  let pair: Table[] = $state([]);
  let selectedTable: Table | null = $state(null);
  let reservations: Reservation[] = $state([]);
  let recommended: Table[] = $state([]);
  let loading = $state(true);
  let error: string | null = $state(null);
  let currentTime: Date = $state(new Date());
  let lastFilter: {
    partySize: number;
    zone: string;
    features: string;
    startTime: string;
  } | null = $state(null);

  onMount(async () => {
    try {
      const [tRes, rRes] = await Promise.all([getTables(), getReservations()]);
      tables = tRes ?? [];
      reservations = rRes ?? [];
    } catch (e) {
      error = "Andmete laadimine ebaõnnestus";
    } finally {
      loading = false;
    }
  });

  async function handleFilter(data: {
    partySize: number;
    zone: string;
    features: string;
    startTime: string;
  }) {
    lastFilter = data;
    await updateRecommendations(
      data.partySize,
      data.zone,
      data.features,
      currentTime,
    );
  }

  async function updateRecommendations(
    partySize: number,
    zone: string,
    features: string,
    time: Date,
  ) {
    try {
      const timeString = time.toISOString().slice(0, 19);
      const res = await getRecommendations(
        partySize,
        timeString,
        zone,
        features,
      );
      if (res.length > 0) {
        recommended = res;
        pair = [];
      } else {
        recommended = [];
        pair = await getTablePairRecommendations(partySize, timeString);
      }
    } catch (e) {
      error = "Soovituste laadimine ebaõnnestus";
    }
  }

  function handleTableClick(table: Table) {
    selectedTable = table;
  }

  function handleBooked(reservation: Reservation) {
    reservations = [...reservations, reservation];
    selectedTable = null;
  }

  let filteredReservations = $derived(
    reservations.filter((r) => {
      const start = new Date(r.startTime);
      const end = new Date(start.getTime() + 2 * 60 * 60 * 1000);
      return currentTime >= start && currentTime <= end;
    }),
  );

  $effect(() => {
    if (lastFilter) {
      updateRecommendations(
        lastFilter.partySize,
        lastFilter.zone,
        lastFilter.features,
        currentTime,
      );
    }
  });
</script>

<main>
  <h1>Restorani reserveerimissüsteem</h1>
  <div class="layout">
    <div class="sidebar">
      <FilterPanel onFilter={handleFilter} />
      <TimeController bind:currentTime />
    </div>
    <div class="content">
      {#if loading}
        <p>Laadimine...</p>
      {:else if error}
        <p class="error">{error}</p>
      {:else}
        <FloorPlan
          {tables}
          reservations={filteredReservations}
          {recommended}
          {pair}
          onTableClick={handleTableClick}
        />
      {/if}
    </div>
  </div>

  {#if selectedTable}
    <BookingModal
      table={selectedTable}
      onClose={() => (selectedTable = null)}
      onBooked={handleBooked}
    />
  {/if}
</main>

<style>
  .layout {
    display: flex;
    gap: 1rem;
    padding: 2rem;
  }
  .sidebar {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  .content {
    flex-grow: 1;
  }
  .error {
    color: red;
  }
</style>

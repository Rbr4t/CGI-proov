<script lang="ts">
  import { onMount } from "svelte";
  import { getTables, getReservations, getRecommendations } from "./lib/api";
  import type { Table, Reservation } from "./lib/types";
  import FilterPanel from "./components/FilterPanel.svelte";
  import FloorPlan from "./components/FloorPlan.svelte";
  import TimeController from "./components/TimeController.svelte";
  import BookingModal from "./components/BookingModal.svelte";

  let tables: Table[] = $state([]);
  let selectedTable: Table | null = $state(null);
  let reservations: Reservation[] = $state([]);
  let recommended: Table[] = $state([]);
  let loading = $state(true);
  let error: string | null = $state(null);
  let currentTime: Date = $state(new Date());

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
    if (data.partySize > 0) {
      try {
        recommended =
          (await getRecommendations(
            data.partySize,
            data.startTime,
            data.zone,
            data.features,
          )) ?? [];
      } catch (e) {
        error = "Soovituste laadimine ebaõnnestus";
      }
    } else {
      recommended = [];
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

  .modal-backdrop {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 100;
  }

  .modal {
    background: white;
    padding: 2rem;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    min-width: 300px;
    color: black;
  }

  .modal h3 {
    margin: 0;
  }

  .modal label {
    display: flex;
    flex-direction: column;
    gap: 4px;
    font-size: 14px;
  }

  .modal input {
    padding: 6px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  .modal-buttons {
    display: flex;
    gap: 0.5rem;
  }

  .modal-buttons button {
    flex: 1;
    padding: 8px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    background: #4caf50;
    color: white;
  }

  .modal-buttons .cancel {
    background: #ef5350;
  }
</style>

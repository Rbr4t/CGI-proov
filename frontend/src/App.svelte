<script lang="ts">
    import { onMount } from 'svelte';
    import { getTables, getReservations, getRecommendations } from './lib/api';
    import type { Table, Reservation } from './lib/types';
    import FilterPanel from './components/FilterPanel.svelte';
    import FloorPlan from './components/FloorPlan.svelte';

    let tables: Table[] = [];
    let reservations: Reservation[] = [];
    let recommended: Table[] = [];
    let loading = true;
    let error: string | null = null;

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

async function handleFilter(data: { partySize: number, zone: string, features: string, startTime: string }) {
    try {
        recommended = await getRecommendations(data.partySize, data.startTime, data.zone, data.features) ?? [];
    } catch (e) {
        error = "Soovituste laadimine ebaõnnestus";
    }
}
</script>

<main>
    <h1>Restorani reserveerimissüsteem</h1>
    <div class="layout">
        <FilterPanel onFilter={handleFilter} />
        <div class="content">
            {#if loading}
                <p>Laadimine...</p>
            {:else if error}
                <p class="error">{error}</p>
            {:else}
                {#if recommended.length > 0}
                    <p class="info">Parima skooriga lauad on esile tõstetud</p>
                {/if}
                <FloorPlan {tables} {reservations} {recommended} />
            {/if}
        </div>
    </div>
</main>

<style>
    .layout { display: flex; gap: 1rem; padding: 2rem; }
    .content { flex-grow: 1; }
    .error { color: red; }
    .info { margin-bottom: 1rem; color: #666; font-style: italic; }
</style>
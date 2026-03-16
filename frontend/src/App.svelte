<script lang="ts">
    import { onMount } from 'svelte';
    import { getTables, getReservations } from './lib/api';
    import type { Table, Reservation } from './lib/types';

    let tables: Table[] = [];
    let reservations: Reservation[] = [];
    let error: string | null = null;
    let loading = true;

    onMount(async () => {
        const tablesRes = await getTables();
        const reservationsRes = await getReservations();

        try {
          tables = tablesRes ?? [];
          reservations = reservationsRes ?? [];
          console.log(tables);
          
        } catch (error) {
          error = error;
        }

        loading = false;
    });

    function isOccupied(tableId: number): boolean {
        return reservations.some(r => r.tableId === tableId);
    }
</script>

<main>
    <h1>Restorani reserveerimissüsteem</h1>

    {#if loading}
        <p>Laadimine...</p>
    {:else if error}
        <p style="color: red;">Viga: {error}</p>
    {:else}
        <div class="tables">
            {#each tables as table}
                <div class="table" class:occupied={isOccupied(table.id)}>
                    <p>Laud #{table.id}</p>
                    <p>{table.capacity} kohta</p>
                    <p>{table.zone}</p>
                    <p>{isOccupied(table.id) ? 'Hõivatud' : 'Vaba'}</p>
                </div>
            {/each}
        </div>
    {/if}
</main>

<style>
    main {
        padding: 2rem;
        font-family: sans-serif;
    }

    .tables {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
        gap: 1rem;
    }

    .table {
        padding: 1rem;
        border: 1px solid #ccc;
        border-radius: 8px;
        background: #e8f5e9;
        cursor: pointer;
    }

    .table.occupied {
        background: #ffebee;
    }
</style>
<script lang="ts">
    import TableNode from './TableNode.svelte';
    import type { Table, Reservation } from '../lib/types';

    export let tables: Table[] = [];
    export let reservations: Reservation[] = [];
    export let recommended: Table[] = [];

    // Arvutame maksimumid automaatselt
    $: maxX = Math.max(...tables.map(t => t.x), 100);
    $: maxY = Math.max(...tables.map(t => t.y), 100);
    
    // Arvutame max skoori reaktiivselt siin
    $: maxScore = recommended.length > 0 
        ? Math.max(...recommended.map(r => r.score ?? 0)) 
        : 0;

    const checkOccupied = (id: number) => reservations.some(r => r.tableId === id);
    const checkRecommended = (id: number) => recommended.some(r => r.id === id && r.score === maxScore);
</script>

<div class="floor-plan-container">
    <div class="floor-plan">
        {#each tables as table}
            <TableNode 
                {table}
                isOccupied={checkOccupied(table.id)}
                isRecommended={checkRecommended(table.id)}
                xPercent={(table.x / maxX) * 100}
                yPercent={(table.y / maxY) * 100}
            />
        {/each}
    </div>
</div>

<style>
    .floor-plan-container { width: 100%; max-width: 1000px; margin: 0 auto; }
    .floor-plan {
        position: relative;
        width: 100%;
        aspect-ratio: 4 / 3; 
        background: #f0f0f0;
        border: 2px solid #333;
        border-radius: 12px;
        background-image: radial-gradient(#ccc 1px, transparent 1px);
        background-size: 20px 20px;
    }
</style>
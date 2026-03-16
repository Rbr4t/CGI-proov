<script lang="ts">
    import type { Table } from '../lib/types';

    let { table, isOccupied, isRecommended, xPercent, yPercent, onTableClick } = $props<{
        table: Table;
        isRecommended: boolean;
        isOccupied: boolean;
        xPercent: number;
        yPercent: number;
        onTableClick: (table: Table) => void;
    }>();
</script>

<div
    class="table"
    class:recommended={isRecommended && !isOccupied}
    class:occupied={isOccupied}
    style="left: {xPercent}%; top: {yPercent}%;"
    onclick={() => !isOccupied ? onTableClick(table) : null}
    role="button"
    tabindex="0"
    onkeydown={(e) => e.key === 'Enter' && onTableClick(table)}

>
    <div class="table-content">
        <span class="id">#{table.id}</span>
        <span class="cap">{table.capacity}p</span>
        <span class="features">{table.features || ''}</span>
        {#if isRecommended && !isOccupied}
            <span class="star">⭐</span>
        {/if}
    </div>
</div>

<style>
    .table { position: absolute; transform: translate(-50%, -50%); width: 10%; min-width: 50px; aspect-ratio: 1 / 1; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 8px; background: #fff; border: 2px solid #999; cursor: pointer; transition: all 0.3s ease; }
    .table.recommended { background: #fff9c4; border-color: #fbc02d; border-width: 4px; z-index: 5; transform: translate(-50%, -50%) scale(1.1); }
    .table.occupied { background: #ffcdd2; border-color: #e53935; color: #b71c1c; cursor: not-allowed }
    .table-content { display: flex; flex-direction: column; font-size: 0.65rem; font-weight: bold; text-align: center; }
    .star { position: absolute; top: -10px; right: -10px; font-size: 1.2rem; }
</style>
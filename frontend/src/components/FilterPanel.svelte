<script lang="ts">
    import DateWidget from './DateWidget.svelte';

    let partySize = $state(2);
    let zone = $state('');
    let features = $state('');
    let date = $state('');
    let time = $state('');

    let { onFilter } = $props<{
        onFilter: (data: { partySize: number, zone: string, features: string, startTime: string }) => void
    }>();

    function handleSubmit() {
        if(!date && !time) return;

        const startTime = `${date}T${time}:00`;
        onFilter({partySize, zone, features, startTime})
    }

</script>

<div class="filter-panel">
    <h2>Otsi lauda</h2>

    <label>
        Kuupäev
        <DateWidget bind:value={date} />
    </label>

    <label>
        Kellaaeg
        <input type="time" bind:value={time}  />
    </label>

    <label>
        Seltskonna suurus
        <input type="number" min="1" max="10" bind:value={partySize} />
    </label>

    <label>
        Tsoon
        <select bind:value={zone}>
            <option value="">Kõik</option>
            <option value="indoor">Sisesaal</option>
            <option value="terrace">Terrass</option>
            <option value="private">Privaatruum</option>
        </select>
    </label>

    <label>
        Eelistus
        <select bind:value={features}>
            <option value="">Pole eelistust</option>
            <option value="window">Aknakoht</option>
            <option value="quiet">Vaikne nurk</option>
            <option value="accessible">Ligipääsetav</option>
        </select>
    </label>

    <button onclick={handleSubmit}>Otsi</button>
</div>

<style>
    .filter-panel {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        padding: 1.5rem;
        border: 1px solid #ccc;
        border-radius: 8px;
        max-width: 300px;
    }

    label {
        display: flex;
        flex-direction: column;
        gap: 4px;
        font-size: 14px;
    }

    input, select {
        padding: 6px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    button {
        padding: 8px;
        background: #4caf50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background: #388e3c;
    }
</style>
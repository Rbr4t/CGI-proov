<script lang="ts">
    let { currentTime = $bindable(new Date()) } = $props<{ currentTime: Date }>();

    function shiftTime(minutes: number) {
        currentTime = new Date(currentTime.getTime() + minutes * 60000);
    }

    function resetToNow() {
        currentTime = new Date();
    }

    let formattedTime = $derived(currentTime.toLocaleTimeString('et-EE', { hour: '2-digit', minute: '2-digit' }));
    let formattedDate = $derived(currentTime.toLocaleDateString('et-EE', { day: '2-digit', month: 'short' }));
</script>

<div class="time-controller">
    <div class="controls">
        <button onclick={() => shiftTime(-60)}>-1h</button>
        <button onclick={() => shiftTime(-15)}>-15m</button>
        <div class="display">
            <span class="time">{formattedTime}</span>
            <span class="date">{formattedDate}</span>
        </div>
        <button onclick={() => shiftTime(15)}>+15m</button>
        <button onclick={() => shiftTime(60)}>+1h</button>
    </div>
    <button class="reset-btn" onclick={resetToNow}>Praegune hetk</button>
</div>

<style>
    .time-controller {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        padding: 1.5rem;
        border: 1px solid #ccc;
        border-radius: 8px;
        max-width: 300px;
    }

    .display {
        display: flex;
        flex-direction: column;
        align-items: center;
        font-size: 14px;
    }

    .time {
        font-size: 1.4rem;
        font-weight: bold;
    }

    .date {
        font-size: 0.75rem;
        opacity: 0.6;
        text-transform: uppercase;
    }

    .controls {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 4px;
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

    .reset-btn {
        width: 100%;
    }
</style>
<script lang="ts">
    // Bindime selle muutuja, et App.svelte saaks seda lugeda
    export let currentTime: Date = new Date();

    function shiftTime(minutes: number) {
        currentTime = new Date(currentTime.getTime() + minutes * 60000);
    }

    function resetToNow() {
        currentTime = new Date();
    }

    $: formattedTime = currentTime.toLocaleTimeString('et-EE', { 
        hour: '2-digit', 
        minute: '2-digit' 
    });
    
    $: formattedDate = currentTime.toLocaleDateString('et-EE', {
        day: '2-digit',
        month: 'short'
    });
</script>

<div class="time-controller">
    <div class="controls">
        <button on:click={() => shiftTime(-60)} title="Tund tagasi">-1h</button>
        <button on:click={() => shiftTime(-15)}>-15m</button>
        
        <div class="display">
            <span class="time">{formattedTime}</span>
            <span class="date">{formattedDate}</span>
        </div>

        <button on:click={() => shiftTime(15)}>+15m</button>
        <button on:click={() => shiftTime(60)} title="Tund edasi">+1h</button>
    </div>
    
    <button class="reset-btn" on:click={resetToNow}>Praegune hetk</button>
</div>

<style>
    .time-controller {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 0.5rem;
        background: #2c3e50;
        padding: 1rem 1.5rem;
        border-radius: 1rem;
        color: white;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    }

    .controls {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .display {
        display: flex;
        flex-direction: column;
        align-items: center;
        min-width: 80px;
        background: rgba(255, 255, 255, 0.1);
        padding: 0.5rem 1rem;
        border-radius: 0.5rem;
    }

    .time {
        font-size: 1.4rem;
        font-weight: bold;
        color: #4db6ac;
    }

    .date {
        font-size: 0.75rem;
        opacity: 0.8;
        text-transform: uppercase;
    }

    button {
        background: #34495e;
        border: none;
        color: white;
        padding: 0.5rem 0.8rem;
        border-radius: 0.4rem;
        cursor: pointer;
        transition: all 0.2s;
    }

    button:hover {
        background: #45627d;
    }

    .reset-btn {
        font-size: 0.8rem;
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.3);
        width: 100%;
    }

    .reset-btn:hover {
        background: rgba(255, 255, 255, 0.1);
    }
</style>
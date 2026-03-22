<script lang="ts">
  let { value = $bindable("") } = $props<{ value: string }>();

  let hours = $state(new Date().getHours());
  let minutes = $state(0);

  const minuteOptions = [...Array(12).keys()].map((x) => x * 5);

  $effect(() => {
    const hh = hours.toString().padStart(2, "0");
    const mm = minutes.toString().padStart(2, "0");
    value = `${hh}:${mm}`;
  });
</script>

<div class="time-widget">
  <select bind:value={hours}>
    {#each Array.from({ length: 24 }, (_, i) => i) as h}
      <option value={h}>{h.toString().padStart(2, "0")}</option>
    {/each}
  </select>
  <span>:</span>
  <select bind:value={minutes}>
    {#each minuteOptions as m}
      <option value={m}>{m.toString().padStart(2, "0")}</option>
    {/each}
  </select>
</div>

<style>
  .time-widget {
    display: flex;
    align-items: center;
  }
  select {
    padding: 0.4rem;
    border-radius: 4px;
    border: 1px solid #ccc;
  }
</style>

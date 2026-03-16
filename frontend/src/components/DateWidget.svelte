<script>
  export let value = ""; // Formatted as YYYY-MM-DD
  
  const currentYear = new Date().getFullYear();
  const months = [
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
  ];

  let selectedMonth = new Date().getMonth(); // 0-indexed
  let selectedDay = new Date().getDate();

  $: daysInMonth = new Date(currentYear, selectedMonth + 1, 0).getDate();
  
  $: if (selectedDay > daysInMonth) selectedDay = daysInMonth;

  $: {
    const mm = (selectedMonth + 1).toString().padStart(2, '0');
    const dd = selectedDay.toString().padStart(2, '0');
    value = `${currentYear}-${mm}-${dd}`;
  }
</script>

<div class="date-widget">
  <select bind:value={selectedMonth}>
    {#each months as month, i}
      <option value={i}>{month}</option>
    {/each}
  </select>

  <select bind:value={selectedDay}>
    {#each Array.from({ length: daysInMonth }, (_, i) => i + 1) as day}
      <option value={day}>{day}</option>
    {/each}
  </select>
  
</div>

<style>
  .date-widget {
    display: flex;
    gap: 0.5rem;
    align-items: center;
  }
  select {
    padding: 0.4rem;
    border-radius: 4px;
    border: 1px solid #ccc;
  }
  
</style>
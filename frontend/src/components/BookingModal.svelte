<script lang="ts">
  import { createReservation } from "../lib/api";
  import type { Table, Reservation } from "../lib/types";
  import TimeWidget from "./TimeWidget.svelte";

  let { table, onClose, onBooked } = $props<{
    table: Table;
    onClose: () => void;
    onBooked: (reservation: Reservation) => void;
  }>();

  let bookingName = $state("");
  let bookingDate = $state("");
  let bookingTime = $state("");
  let bookingError = $state("");

  async function handleBook() {
    if (!bookingName || !bookingDate || !bookingTime) {
      bookingError = "Palun täida kõik väljad!";
      return;
    }
    try {
      const startTime = `${bookingDate}T${bookingTime}:00`;
      const res = await createReservation({
        tableId: table.id,
        partySize: 2,
        startTime,
        customerName: bookingName,
      });
      onBooked(res);
    } catch (e) {
      bookingError = "Broneerimine ebaõnnestus!";
    }
  }
</script>

<div class="modal-backdrop" onclick={onClose}>
  <div class="modal" onclick={(e) => e.stopPropagation()}>
    <h3>Broneeri laud #{table.id} ({table.capacity} kohta)</h3>

    <label>
      Nimi
      <input type="text" bind:value={bookingName} placeholder="Sinu nimi" />
    </label>

    <label>
      Kuupäev
      <input type="date" bind:value={bookingDate} />
    </label>

    <label>
      Kellaaeg
      <TimeWidget bind:value={bookingTime} />
    </label>
    <div class="disclaimer">
      <p style="font-size: small;">*Reserveering kestab 2h</p>
      <p style="font-size: small;">**Mitut lauda tuleb eraldi reserveerida</p>
    </div>

    {#if bookingError}
      <p class="error">{bookingError}</p>
    {/if}

    <div class="modal-buttons">
      <button onclick={handleBook}>Broneeri</button>
      <button class="cancel" onclick={onClose}>Tühista</button>
    </div>
  </div>
</div>

<style>
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
    background: #16171d;
    border: 1px solid #ccc;
    padding: 2rem;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    min-width: 300px;
    color: white;
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

  .error {
    color: #ef5350;
  }

  .disclaimer {
    display: flex;
    flex-direction: column;
    gap: 0;
  }

  .disclaimer > p {
    padding: 0;
    margin: 0;
    gap: 0;
  }
</style>

<template>
  <div class="p-4">
    <GrowCard :loading="loading" class="enter-y" />

    <div class="md:flex enter-y mt-4">
      <ErrorPie class="md:w-1/3 mr-4 w-full" :loading="loading" />
      <ErrorLine class="md:w-2/3 !ml-4 w-full"  :loading="loading" />
    </div>

    <div class="md:flex enter-y mt-4">
      <RequestBar class="md:w-1/3 mr-4 w-full" :loading="loading" />
      <RequestLine class="md:w-2/3 !ml-4 w-full"  :loading="loading" />
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import GrowCard from './components/GrowCard.vue';
  import ErrorLine from "./components/ErrorLine.vue";
  import ErrorPie from "./components/ErrorPie.vue";
  import RequestBar from "./components/RequestBar.vue";
  import RequestLine from "./components/RequestLine.vue";

  export default defineComponent({
    components: {
      GrowCard,
      ErrorPie,
      ErrorLine,
      RequestBar,
      RequestLine,
    },
    setup() {
      const loading = ref(true);

      setTimeout(() => {
        loading.value = false;
      }, 1500);
      return { loading };
    },
  });
</script>

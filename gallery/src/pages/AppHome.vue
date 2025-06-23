<template>
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
    <div class="col" v-for="(item, idx) in state.items" :key="idx">
     <AppCard :item="item"/>
    </div>
    </div>
    </div>
    </div>
</template>

<script>

import AppCard from '@/components/AppCard.vue';
import axios from 'axios';
import { reactive } from 'vue';

export default {
    name: "AppHome",
    components: {AppCard},
    setup(){
      const state = reactive({
         items:[]
      })
  
      axios.get("/api/items").then(({data}) => {
        state.items = data;
      })

      return {state}
    }
}

</script>

<style scoped>

</style>
<template>
  <div class="cart">
    <div class="container">
     <ul>
    <li v-for="(i, idx) in state.items" :key="idx">
      <img :src="i.imgPath" class="img" />
      <span class="name">{{ i.name }}</span>
      <span class="price">{{ lib.getNumberFormatted(i.price - i.price * i.discountPer / 100) }}원</span>
      <i class="fa fa-trash" @click="remove(i.id)"></i>
    </li>
     </ul>
     <router-link to="/order" class="btn btn-primary">구입하기</router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { reactive } from 'vue';
import lib from '@/scripts/lib';

export default {
  setup() {
    const state = reactive({
      items:[]
    })

    const load = () =>{
    axios.get("/api/cart/items").then(({data}) => {
      console.log(data);
      state.items=data;
    })
  };

  const remove = (itemId) =>{
   axios.delete(`/api/cart/items/${itemId}`).then(()=>{
    load();
   }) 
  }
  load();

  return {state, lib, remove}
}
}
</script>

<style scoped>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

li {
  border: 1px solid #eee;
  margin-top: 25px;
  margin-bottom: 25px;
  padding: 10px;
  display: flex;
  align-items: center;
}

img {
  width: 150px;
  height: 150px;
  object-fit: cover;
}

.name {
  margin-left: 25px;
  font-weight: bold;
  font-size: 16px;
}

.price {
  margin-left: 25px;
  color: #333;
  font-size: 15px;
}

i {
  margin-left: auto;
  font-size: 20px;
  cursor: pointer;
}

.btn {
  width: 300px;
  display: block;
  margin: 30px auto 0 auto;
  padding: 15px 30px;
  font-size: 20px;
  text-align: center;
}
</style>
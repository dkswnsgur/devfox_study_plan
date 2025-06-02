<template>
  <Navbar />
  <Event :text="text[eventTextNum]" />
   {{ eventTextNum }}
  <SearchBar :data="data_temp" @searchMovie="searchMovie($event)" />
  <p>
    <button @click="showAllMovie">전체보기</button>
  </p>
  <Movies :data="data_temp"
   @openModal="isModal=true; selectedMovie=$event"
   @increseLike="increseLike($event)"
  />

<Modal :data="data" :isModal="isModal" :selectedMovie="selectedMovie" @closeModal="isModal=false" /> 
</template>

<script>
import data from './assets/movies'; //영화 데이터
import Navbar from './components/Navbar.vue';
import Modal from './components/Modal.vue';
import Event from './components/Event.vue';
import SearchBar from './components/SearchBar.vue';
import Movies from './components/Movies.vue';

console.log(data);  

 export default {
  name: 'APP',
  data() {
    return {
      isModal: false,
      data: data,
      data_temp: [...data], //사본
      selectedMovie: 0,
      text: ['NETPLX 강렬한 운명의 드라마, 경기 크리쳐!!!','디즈니 100주년 기념작','그날 대한민국의 운명이결정됬다'],
      eventTextNum: 0,
      interval: null,
    }
  },
 methods: {
   increseLike(id) {
    // this.data[i].like += 1;
    this.data.find(movie => {
      if(movie.id == id) {
        movie.like += 1;
      }
    })
   },
   searchMovie(title) {
    //영화제목이 포함된 데이터를 가져옴
    this.data_temp = this.data.filter(movie => {
      return movie.title.includes(title);
    })
   },
   showAllMovie() {
    this.data_temp = [...this.data];
   }

 },
 components: {
  Navbar: Navbar,
  Event: Event,
  Modal: Modal,
  Movies: Movies,
  SearchBar: SearchBar,
 },
  mounted() {
    console.log('mounted');
    this.interval = setInterval(() => {
      if (this.eventTextNum === this.text.length - 1) {
        this.eventTextNum = 0;
      } else {
        this.eventTextNum += 1;
      }
    }, 3000);
  },

  unmounted() {
    clearInterval(this.interval);
  }
}

  
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
}

body {
  max-width: 768px;
  margin: 0 auto;
  padding: 20px;
}

h1, h2, h3 {
  margin-bottom: 1rem;
}

p {
  margin-bottom: 0.5rem;
}

button {
  margin-right: 10px;
  margin-top: 1rem;
}

.item {
  width: 100%;
  border: 1px solid #ccc;
  display: flex;
  margin-bottom: 20px;
  padding: 1rem;
}

.item figure {
  width: 30%;
  margin-right: 1rem;
}

.item img {
  width: 100%;
}

.item .info {
  width: 100%;
}

.modal {
  background : rgba(0, 0, 0, 0,7);
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal .inner {
  background-color: #ffff;
  width: 80%;
  padding: 20px;
  border-radius: 10px;
}
</style>

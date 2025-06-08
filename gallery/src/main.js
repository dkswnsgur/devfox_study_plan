import { createApp } from 'vue'
import App from './App.vue'
import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {path:'/', component: AppHome}
]

const router = createRouter({history: createWebHistory(), routes})

createApp(App).use(router).mount('#app')

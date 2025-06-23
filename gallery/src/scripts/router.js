import {createRouter, createWebHistory} from 'vue-router'
import AppHome from '../pages/AppHome.vue'
import AppLogin from '../pages/AppLogin.vue'
import AppCart from '../pages/AppCart.vue'
import AppOrder from '../pages/AppOrder.vue'
import AppOrders from '@/pages/AppOrders.vue'

const routes = [
    {path:'/', component: AppHome},
    {path:'/login', component: AppLogin},
    {path:'/cart', component: AppCart},
    {path:'/order', component: AppOrder},
    {path:'/orders', component: AppOrders}
]

const router = createRouter({history: createWebHistory(), routes})

export default router;

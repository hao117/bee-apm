import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/pages/Index'
import Item1 from '@/pages/Item1'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/ui/index.html',
      name: 'Index',
      component: Index
    },
    {
      path: '/ui/item1',
      name: 'Item1',
      component: Item1
    }
  ]
})

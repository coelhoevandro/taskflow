import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/pages/LoginPage.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/AppLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', name: 'dashboard', component: () => import('@/pages/DashboardPage.vue') },
      { path: 'tasks', name: 'tasks', component: () => import('@/pages/TaskListPage.vue') },
      { path: 'tasks/new', name: 'task-create', component: () => import('@/pages/TaskFormPage.vue') },
      { path: 'tasks/:id', name: 'task-detail', component: () => import('@/pages/TaskDetailPage.vue') },
      { path: 'tasks/:id/edit', name: 'task-edit', component: () => import('@/pages/TaskFormPage.vue') },
      { path: 'notifications', name: 'notifications', component: () => import('@/pages/NotificationsPage.vue') },
      { path: 'profile', name: 'profile', component: () => import('@/pages/ProfilePage.vue') },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (!to.meta.public && !auth.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  if (to.name === 'login' && auth.isAuthenticated) {
    return { name: 'dashboard' }
  }
})

export default router

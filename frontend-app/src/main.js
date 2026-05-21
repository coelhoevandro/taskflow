import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

import App from './App.vue'
import router from './router'

const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#3B5BDB',
          secondary: '#6741D9',
          surface: '#FFFFFF',
          background: '#F4F6FA',
          error: '#E03131',
          warning: '#F08C00',
          success: '#2F9E44',
          info: '#1971C2',
        },
      },
    },
  },
  defaults: {
    VCard: { rounded: 'lg' },
    VBtn: { rounded: 'lg' },
    VTextField: { variant: 'outlined', density: 'comfortable' },
    VSelect: { variant: 'outlined', density: 'comfortable' },
    VTextarea: { variant: 'outlined', density: 'comfortable' },
  },
})

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)

// Restaura sessão do localStorage ANTES do router processar a primeira navegação.
// Sem isso, o guard veria isAuthenticated=false e redirecionaria pro login
// mesmo quando o usuário já tem um token válido (ex: ao dar F5).
import { useAuthStore } from '@/stores/auth'
useAuthStore(pinia).initFromStorage()

app.use(router)
app.use(vuetify)
app.mount('#app')

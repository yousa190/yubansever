import { defineStore } from 'pinia'

export const useCommonStore = defineStore('common', {
  state: () => ({
    shouldScrollTop: false
  }),
  actions: {
    setScrollTop(value) {
      this.shouldScrollTop = value
    }
  }
})

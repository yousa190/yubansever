import { defineStore } from 'pinia';
import { ref } from 'vue';

const initState=()=>{
  return {
      categoryList:[],
    
  }
}

export const useAllDataStore =defineStore('userinfo',()=>{
  const state = ref(initState())

  function updateUser(user) {
    state.value.user.name = user.name
    state.value.user.token = user.token
  }


  return {
    state,
    updateUser
  }
})

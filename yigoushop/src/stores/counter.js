import { defineStore } from 'pinia';
import { ref } from 'vue';

const initState=()=>{
  return {
    user:{
      name:'yuban',
      token:'114514'
    }
  }
}

export const useAllDataStore =defineStore('allData',()=>{
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


const user_tgle=document.querySelector(".user-img");
const u_menu =document.querySelector('.Top-User-menu');

console.log(user_tgle);
console.log(u_menu);
user_tgle.addEventListener('mouseover',()=>{
   u_menu.classList.add('u-m-active');
})
user_tgle.addEventListener('mouseleave',()=>{
   u_menu.classList.remove('u-m-active');
})
u_menu.addEventListener('mouseover',()=>{
   u_menu.classList.add('u-m-active');
})
u_menu.addEventListener('mouseleave',()=>{
   u_menu.classList.remove('u-m-active');
})

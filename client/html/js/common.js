
const user_tgle=document.querySelector(".user-img");
const u_menu =document.querySelector('.Top-User-menu');
if(user_tgle)user_tgle.addEventListener('mouseover',()=>{
   u_menu.classList.add('u-m-active');
})
if(user_tgle)user_tgle.addEventListener('mouseleave',()=>{
   u_menu.classList.remove('u-m-active');
})
if(u_menu)u_menu.addEventListener('mouseover',()=>{
   u_menu.classList.add('u-m-active');
})
if(u_menu)u_menu.addEventListener('mouseleave',()=>{
   u_menu.classList.remove('u-m-active');
})

//小提示框
let tooltip=document.querySelector('.Tooltip');

let t_cnt=0;
function tipevent(msg ,bgcolor){
   if (t_cnt<3) {
      let newms = document.createElement('div');
      newms.innerHTML = msg;
      newms.classList.add('little_tip');
      newms.style.backgroundColor = `${bgcolor}`;
      tooltip.insertBefore(newms, tooltip.children[0]);
      t_cnt++;
      setInterval(e => {
         newms.classList.add('tip_show')
      }, 1)
      setInterval(e => {
         newms.classList.remove('tip_show')
      }, 3000)

      setTimeout(e=>{
         newms.remove();
      },3000)
      t_cnt--;
   }
}
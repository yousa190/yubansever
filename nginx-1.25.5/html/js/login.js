let swiCtn=document.querySelector('#change-btn');
let sc1 =document.querySelector('#switch-tran-a');
let sc2=document.querySelector('#switch-tran-b');
let swcircle =document.querySelectorAll('.switch-cir');
let switchBtns=document.querySelectorAll('.switch-btn');
let acon =document.querySelector('#a-sign_container');
let bcon =document.querySelector('#b-login_container');
let allbtns =document.querySelectorAll('.submit');
let getBtns =(e)=>e.preventDefault();


let changeF =(e)=> {
    console.log(changeF)

    swiCtn.classList.add('is-gx');
    setTimeout(()=>{
        swiCtn.classList.remove('is-gx');
    },1500)
    swiCtn.classList.toggle('is-txr');
    swcircle[0].classList.toggle('is-txr');
    swcircle[1].classList.toggle('is-txr');

    sc1.classList.toggle('is-hidden');
    sc2.classList.toggle('is-hidden');
    acon.classList.toggle('is-txl');
    bcon.classList.toggle('is-txl');
    bcon.classList.toggle('is-z');
}

let change =()=>{
    for (let i=0;i<allbtns.length;++i)
        allbtns[i].addEventListener('click',getBtns);
    for (let i = 0; i < switchBtns.length; i++)
        switchBtns[i].addEventListener('click',changeF);
}
window.addEventListener('load',change);
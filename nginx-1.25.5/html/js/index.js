/* swiper */
new Swiper(".mySwiper", {
    spaceBetween: 30,
    centeredSlides: true,
    loop: true,
    autoplay: {
        delay: 2500,
        disableOnInteraction: false,
    },
    initialSlide : 0,
    observer:true,
    pagination: {
        el: ".swiper-pagination",
        clickable: true,
    },
    navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
    },

})
for(let i = 0; i <9 ;++i){
    if(i+1===9 || i === 0 ){
        document.querySelectorAll(`#slide${i+1}`).forEach(e=>{
            e.innerHTML=`<a href="#" id='slide1-url'>
        <img src="./images/SWIPER1-TEMP.png" alt="" id ='slide1-img'>
      <div class="info" id="slide1-info">
      【新】【YUZUSOFT】天使☆騒々 RE-BOOT! 天使☆骚骚 RE-BOOT! 
      </div>
      </a>`
        })
    }
    else{
        document.querySelector(`#slide${i+1}`).innerHTML=`<a href="#" id='slide1-url'>
        <img src="./images/SWIPER1-TEMP.png" alt="" id ='slide1-img'>
      <div class="info" id="slide1-info">
      【新】【YUZUSOFT】天使☆騒々 RE-BOOT! 天使☆骚骚 RE-BOOT! 
      </div>
      </a>`
    }
}
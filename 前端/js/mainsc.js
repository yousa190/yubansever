const canvas =document.querySelector('canvas');
/* 设置画布宽高 */
canvas.width =window.innerWidth;
canvas.height =window.innerHeight;



/*使用2D绘图 */
const ctx =canvas.getContext('2d');
/* 樱花数量 */
const sakura_sum =40;
const sakuraArr =[];

/* sakura class */
class sakura{
    constructor(){
        this.x =Math.random()*canvas.width;
        this.y =(Math.random()*canvas.height*2)-canvas.height;
        /* 随机生成花瓣宽高 */
        this.width =Math.random()*15+25;
        this.height =Math.random()*15+20;
        //透明度
        this.opacity =this.width/50;
        //设置随机数
        this.rotate=Math.random();
        //speed
        this.xSpeed =Math.random()*0.5+1;
        this.ySpeed =Math.random()+1.5;
        this.rotateSpeed =Math.random()*0.02;
    }
//draw
    draw(){
        /* 防止超过canvas */
        if(this.x>canvas.width||this.y>canvas.height){
            this.x =-sakuraImg.height;
            this.y =(Math.random()*canvas.height*2)-canvas.height;
            this.rotate=Math.random();
            this.xSpeed =Math.random()*2+1;
            this.ySpeed =Math.random()+1.5;
            this.rotateSpeed =Math.random()*0.02;
        }
        //canvas透明基数
        ctx.globalAlpha =this.opacity;
        ctx.drawImage(
            sakuraImg,
            this.x,
            this.y,
            this.width*(0.6+Math.abs(Math.cos(this.rotate))/3),
            this.height*(0.8+Math.abs(Math.sin(this.rotate))/5)
        );
    }
    animate() {
        this.x +=this.xSpeed +mouseX*5;
        this.y +=this.ySpeed +mouseX*2;
        this.rotate+=this.rotateSpeed;
        this.draw();

    }
}

/* 渲染 */
function render(){
    /* 清空屏幕 */
    ctx.clearRect(0,0,canvas.width,canvas.height);
    //  
    sakuraArr.forEach(sakura =>sakura.animate());
    //告诉浏览器调用指定函数进行渲染
    window.requestAnimationFrame(render);

}
//加载图片
const sakuraImg =new Image();
sakuraImg.src = './images/sakura.jpg';
sakuraImg.addEventListener('load',()=>{
    for(let i=0;i<sakura_sum;++i){
        sakuraArr.push(new sakura());
    }
    render();  
})

let mouseX =0;
//移动监听
function touchHeader(e){
    //客户端区域的水平座标
    mouseX =(e.clientX||e.touches[0].clientX)*0.5/window.innerWidth;
}
window.addEventListener('mousemove',touchHeader);
window.addEventListener('touchmove',touchHeader);

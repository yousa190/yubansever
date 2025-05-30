let swiCtn=document.querySelector('#change-btn');
let sc1 =document.querySelector('#switch-tran-a');
let sc2=document.querySelector('#switch-tran-b');
let swcircle =document.querySelectorAll('.switch-cir');
let switchBtns=document.querySelectorAll('.switch-btn');
let acon =document.querySelector('#a-sign_container');
let bcon =document.querySelector('#b-login_container');
let allbtns =document.querySelectorAll('.submit');
let getBtns =(e)=>e.preventDefault();
let chan_f=0
let cre=document.querySelector('#sign-create-form');
let foun=document.querySelector('#sign-found-form');
let reset=document.querySelector('#sign-reset-form');
function change_resetpwd(bool){
    if (bool===false){
        foun.classList.remove('is-hidden');
        reset.classList.add('is-hidden');

    }
    else{
        reset.classList.remove('is-hidden');
        foun.classList.add('is-hidden');
    }
    chan_f=0;
}

let changeF =(e)=> {
    if (chan_f===0){
        cre.classList.remove('is-hidden');
        foun.classList.add('is-hidden');
        reset.classList.add('is-hidden');
    }
    // 账号找回
    else if (chan_f===1){
        cre.classList.add('is-hidden');
        change_resetpwd(false);
    }
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

    document.querySelector('.form-link').addEventListener('click',e=>{
        chan_f=1;
        changeF();
    });
}
window.addEventListener('load',change);


let found =document.querySelector("#sign-found-form");
found.classList.add("is-hidden");

/*注册事件*/
let submit =document.querySelector('#sign');

let user_name=document.querySelector('#sign_name');
let sign_email=document.querySelector('#sign_email');
let sign_pwd=document.querySelector('#sign_pwd');
let warn_1=document.querySelector('#warn-1');
let warn_2=document.querySelector('#warn-2');
let warn_3=document.querySelector('#warn-3');
// 检测注册表

let s_f1=1
let s_f2=1
let s_f3=1
user_name.addEventListener('mouseleave',evt => {
    let pattern = /@/;
    if (pattern.test(user_name.value)){
        warn_1.innerHTML='名称有非法字符!'
        warn_1.classList.remove('warn-hidden');
    }
    else if (user_name.value!=null||user_name.value!==''){
    //     进行检测
        axios.get(`http://localhost:8080/login/check_name`,
            {
                params:{
                    user_name:user_name.value
                }
            }
            )
            .then(res=>{
                if (res.data.data>0){
                    s_f1=1
                    warn_1.innerHTML='昵称已经被使用!';
                    warn_1.classList.remove('warn-hidden');
                }
                else  {
                    s_f1=0
                    warn_1.classList.add('warn-hidden')
                }
            }).catch(e=>{
            s_f1=1
            warn_1.innerHTML='服务器错误!';
            warn_1.classList.remove('warn-hidden');
        })
    }
    else{
        s_f1=1
        warn_1.innerHTML='';
        warn_1.classList.add('warn-hidden')
    }
})

// 邮箱正则表达式
const reg=/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/


sign_email.addEventListener('mouseleave',evt => {

    if (sign_email.value!=null||sign_email.value!==''){
        //     进行检测
        if(reg.test(sign_email.value)){
            warn_2.classList.add('warn-hidden')
            axios.get(`http://localhost:8080/login/check_email`,
                {
                    params:{
                       email:sign_email.value
                    }
                }
            ).then(res=>{
                if (res.data.data>0){
                    s_f2=1
                    warn_2.innerHTML='邮箱已经被使用!'
                    warn_2.classList.remove('warn-hidden');
                }
                else  {
                    s_f2=0
                    warn_2.classList.add('warn-hidden')
                }
            }).catch(e=> console.log(e))
        }
        else{
            s_f2=1
            warn_2.innerHTML=!sign_email.value?'邮箱为空!':'邮箱格式不正确!'
            warn_2.classList.remove('warn-hidden')
        }
    }
})




let reg2=  /^(?=.*\d)(?=.*[A-Za-z]|.*[-+_!@#$%^&*.,?])[A-Za-z\d\-+_!@#$%^&*.,?]{6,}$/;

sign_pwd.addEventListener('mouseleave',evt => {
    if (sign_pwd.value){
        if (reg2.test(sign_pwd.value)){
            s_f3=0;
            warn_3.classList.add('warn-hidden')
        }
        else {
            s_f3=1;
            warn_3.innerHTML=!sign_pwd?'密码为空!':'密码太简单!'
            warn_3.classList.remove('warn-hidden')
        }
    }
    else {
        warn_3.classList.add('warn-hidden')
    }
})

// 提交注冊表单
submit.addEventListener('click',evt => {
    let params = new FormData()
    params.append('userName',user_name.value)
    params.append('email',sign_email.value)
    params.append('pwd',sign_pwd.value)
    if (s_f1===0&&s_f2===0&&s_f3===0){
        axios.post('http://localhost:8080/login/sign',params,{
            headers: {'Content-Type': 'multipart/form-data'}
        })
            .then(e=>{
                if(e.data.data==='true')tipevent('注冊成功','green');
        }).catch(err=>{console.log(err)})
    }
    else{
      tipevent('注冊信息有誤!!!','red');
    }
})

// 登錄檢測(未實現token)

let lgn_name =document.querySelector('#lgn_name');
let lgn_pwd =document.querySelector('#lgn_pwd');
let lgn_warn=document.querySelector('#login_warn');
document.querySelector('#login').addEventListener('click',e=>{
    if (lgn_name.value){
        lgn_warn.classList.add('warn-hidden');
        if (lgn_pwd.value){
            let params = new FormData()
            params.append('name',lgn_name.value)
            params.append('pwd',lgn_pwd.value)
            axios.post('http://localhost:8080/login',params,{
                headers: {'Content-Type': 'multipart/form-data'}
            })
                .then(e=>{
                  if (e.data.errno===1){
                      tipevent('登录成功','green');
                      //     !!!!!
                  }
                  else if (e.data.errno===0){
                      tipevent(`${e.data.message}`,'red');
                      //     !!!!!
                  }
                })
                .catch(e=>{
                console.log(e)})
        }
        else{
            lgn_warn.classList.remove('warn-hidden');
            lgn_warn.innerHTML='密码为空!';
        }
    }
    else{
        lgn_warn.classList.remove('warn-hidden');
        lgn_warn.innerHTML='姓名或邮箱为空!';
    }
})


// 密码找回

function email_Check(email){
    let warn_spe =document.getElementById('warn-spe');
    // warn_spe.classList.add('warn-hidden')
    if (email.value!==''&&reg.test(email.value)){

        //     进行检测
            axios.get(`http://localhost:8080/login/yanzheng`,
                {
                    params:{
                        email:email.value
                    }
                }
            ).then(res=>{
                console.log(res)
                if (res.data.data===0){
                    warn_spe.innerText='邮箱有误!'
                    warn_spe.classList.remove('warn-hidden')
                    return false;
                }
                else warn_spe.classList.add('warn-hidden')
            }).catch(e=> console.log(e))
    }else{
        warn_spe.innerText='邮箱有误!'
        warn_spe.classList.remove('warn-hidden')
        return false;
    }
    return true;
}

let foundemail=document.getElementById('found-2');
let code=document.getElementById('code');
let getCode=document.getElementById('get_code');
let get_flag =1;


// 设置按钮虚化
function settime($obj, time) {
    if (time == 0) {
        $obj.setAttribute("disabled", false);
        $obj.background= "#f38401";
        $obj.cursor="pointer";
        $obj.text("获取手机验证码");
        return;
    } else {
        $obj.setAttribute("disabled", true);
        $obj.background="#ccc";
        $obj.cursor="not-allowed";
        time--;
    }
    setTimeout(function () { settime($obj, time) }, 1000)
}


getCode.addEventListener('click' ,ev => {
 if (get_flag===1 && email_Check(foundemail)){
        let count =60;
        getCode.classList.add('grey');
        let te= setInterval(e=>{
            getCode.innerText=count;
            count--;
            if(!count)clearInterval(te);
        },1000);
        getCode.innerText="获取验证码";
        axios.get('http://localhost:8080/email/code', {
            params:{
                email:foundemail.value
            }
        }).then(res=>{
            tipevent('发送成功','green');
        }).catch(e=>{
            tipevent('发送失败','red');
        })
        settime(getCode,60);
    }
})

let codeCheck=(code)=>{
    let warnC=document.getElementById('warn-spe-b');
    if (code.value==''||!code.value){
        warnC.innerText='验证码为空!'
        warnC.classList.remove('warn-hidden')
    }
    else warnC.classList.add('warn-hidden')
}

let finalemail=null;

document.getElementById('find').addEventListener('click',ev => {
    finalemail=foundemail.value;
    if (code.value!=''){
        let params = new FormData()
        params.append('email',foundemail.value)
        params.append('code',code.value)
        axios.post('http://localhost:8080/login/find',params
        ).then(res=>{
            console.log(res)
            if (res.data.errno===0){
                tipevent(res.data.message,'red');
            }
            else if (res.data.errno===1){
                tipevent(res.data.message,'green');
                setTimeout(change_resetpwd(true),2000);
            }
        }).catch(e=>{
            tipevent('请求失败!','red');
        })
    }
    else{
        email_Check(foundemail.value)
        codeCheck(code);
    }
})

let finalPwd=null;
// 密码重设
let resetCheck=()=>{
    let resP1=document.getElementById('reset-1');
    let resP2=document.getElementById('reset-2');
    let resWarn1=document.getElementById('warn-reset-1');
    let resWarn2=document.getElementById('warn-reset-2');
    let f1 =0;
    let f2 =0;
    if (resP1.value){
        if (reg2.test(resP1.value)){
            resWarn1.classList.add('warn-hidden')
        }
        else {
            resWarn1.innerHTML=!resP1.value?'密码为空!':'密码太简单!'
            resWarn1.classList.remove('warn-hidden')
            f1=1;
        }
    }
    else {
        resWarn1.classList.add('warn-hidden')
    }

    if (resP2.value){
        if (resP1.value!==resP2.value){
            resWarn2.innerHTML='密码不同!';
            resWarn2.classList.remove('warn-hidden');
            f2=1;
        }
        else{
            resWarn2.classList.add('warn-hidden');
        }
    }

    if (f1===0&&f2===0){
        finalPwd=resP1.value;
        return true;
    }

    return false;
}

document.getElementById('resetcheck').addEventListener('click',ev => {
    if (resetCheck()){
        let params=new FormData();
        params.set('email','19959771806@163.com');
        params.set('pwd',finalPwd);
       axios.post('http://localhost:8080/login/reset', params)
           .then(res=>{
               console.log(res)
               if (res.data.data===true){
                   tipevent('登陆成功','green');
               }
               else  tipevent('登陆失败','red');
           })
           .catch(err=>{
               tipevent('登陆失败','red');
       })
    }
});
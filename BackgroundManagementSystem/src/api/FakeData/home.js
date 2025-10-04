export default {
    tableData: ()=>{
        return{
            code: '00000',
            data:{
                tableData:[
                    {
                        name: "aGood",
                        count: 100,
                        price: 20.0,
                        total: 11
                    },
                    {
                        name: "bGood",
                        count: 100,
                        price: 20.0,
                        total: 11
                    },
                    {
                        name: "cGood",
                        count: 100,
                        price: 20.0,
                        total: 11
                    }
                ]
            }
        }
    },
    countData: ()=>{
        return {
            code: '00000',
            data:[{
                name: "已支付订单",
                value: 123,
                icon: "SuccessFilled",
                color: "#2ec7c9"
                },{
                name: "收藏订单",
                value: 3333,
                icon: "StarFilled",
                color: "#ffb980"
                },{
                name: "未支付订单",
                value: 1233,
                icon: "GoodsFilled",
                color: "#5ab1ef"
                },{
                name: "已支付订单",
                value: 123,
                icon: "SuccessFilled",
                color: "#2ec7c9"
            },{
                name: "收藏订单",
                value: 3333,
                icon: "StarFilled",
                color: "#ffb980"
            },{
                name: "未支付订单",
                value: 1233,
                icon: "GoodsFilled",
                color: "#5ab1ef"
            }
            ]
        }
    },
    chartData:()=>{
        return {
            code: '00000',
            data: {
                orderData: {
                    date :[
                        "2025-01-20",
                        "2025-01-21",
                        "2025-01-22",
                        "2025-01-23",
                        "2025-01-24",
                        "2025-01-25",
                        "2025-01-26",

                    ],
                    data:[
                        {
                            "oppo": 2876,
                            "xiaomi": 3195,
                            "huawei": 4508,
                            "vivo": 3741,
                            "oneplus": 4978,
                            "iphone": 5632
                        },
                        {
                            "oppo": 2345,
                            "xiaomi": 4321,
                            "huawei": 3987,
                            "vivo": 5109,
                            "oneplus": 5991,
                            "iphone": 4756
                        },
                        {
                            "oppo": 3267,
                            "xiaomi": 2827,
                            "huawei": 3629,
                            "vivo": 4810,
                            "oneplus": 5823,
                            "iphone": 5964
                        },
                        {
                            "oppo": 4216,
                            "xiaomi": 3984,
                            "huawei": 5231,
                            "vivo": 3456,
                            "oneplus": 4650,
                            "iphone": 5123
                        },
                        {
                            "oppo": 5149,
                            "xiaomi": 2994,
                            "huawei": 4371,
                            "vivo": 3287,
                            "oneplus": 4990,
                            "iphone": 5210
                        },
                        {
                            "oppo": 6117,
                            "xiaomi": 3889,
                            "huawei": 4692,
                            "vivo": 4021,
                            "oneplus": 4321,
                            "iphone": 5778
                        },
                        {
                            "oppo": 2294,
                            "xiaomi": 3654,
                            "huawei": 4293,
                            "vivo": 3985,
                            "oneplus": 5012,
                            "iphone": 4890
                        }
                    ],
                },
                videoData:[
                    { name: "xiaomi",value:2999},
                    { name: "iphone",value:12999},
                    { name: "oppo",value:32999},
                    { name: "redmi",value:42999},
                    { name: "huawei",value:52999},
                    { name: "vivo",value:72999},
                    { name: "oneplus",value:42999},
                    { name: "honor",value:12999},
                ],
                userData:[
                    {date:"周一",new:1,active:100},
                    {date:"周二",new:2,active:200},
                    {date:"周三",new:3,active:300},
                    {date:"周四",new:4,active:400},
                    {date:"周五",new:5,active:500},
                    {date:"周六",new:6,active:600},
                    {date:"周日",new:7,active:700},
                ]
            }


        }
    }
}

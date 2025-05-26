const env =import.meta.env.MODE || "prod"
const EnvConfig ={
    development:{
        baseApi: "http://localhost:8080",
        mockApi: "/api",
    },

}

export default {
    env,
    ...EnvConfig[env],
//     mock
    mock:false
}
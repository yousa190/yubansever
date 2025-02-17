const env =import.meta.env.MODE || "prod"
const EnvConfig ={
    development:{
        baseApi: "/api",
        mockApi: "/apia",
    },

}

export default {
    env,
    ...EnvConfig[env],
//     mock
    mock:false
}
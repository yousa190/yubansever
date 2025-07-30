import request from "@/api/axiosRequest.js";

export default {
    // sample(){
    //   return request({
    //   url:,
    //   method:,
    //   data:,
    //   mock:,
    // })
    // }
    getSearchNavLayerCategoryList(){
      return request({
        url:'/home/layercategorylist',
        method:'GET',
        data:{},
        mock:true
      });

    }


}

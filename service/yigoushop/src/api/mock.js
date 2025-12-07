import Mock from  'mockjs'
import fakeHomeData from '@/api/fakeData/fakeHomeData.js'


Mock.mock(/api\/home\/layercategorylist/,"get",fakeHomeData.categoryResponse)

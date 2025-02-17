import Mock from  'mockjs'
import Fakehome from './FakeData/home.js'
import Fakeuser from './FakeData/user.js'
import MenuApi from './FakeData/permission.js'
// 拦截路径 方法 制造数据
Mock.mock(/api\/home\/tabledata/,"get",Fakehome.tableData)
Mock.mock(/api\/home\/countdata/,"get",Fakehome.countData)
Mock.mock(/api\/home\/chartdata/,"get",Fakehome.chartData())

Mock.mock(/api\/home\/userinfodata/,"get",Fakeuser.getuserList)
Mock.mock(/api\/home\/deleteuser/,"get",Fakeuser.deleteUser)
Mock.mock(/api\/home\/adduser/,"post",Fakeuser.addUser)
Mock.mock(/api\/home\/edituser/,"post",Fakeuser.editUser)
Mock.mock(/permission\/getmenu/,"post",MenuApi.getMenu)
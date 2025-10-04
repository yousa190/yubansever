<script setup lang="js">
import { Box, House, Service, Select } from '@element-plus/icons-vue'
</script>

<!-- src/components/common/Footer.vue -->
<template>
  <footer class="app-footer">
    <div class="footer-content">
      <el-card>
        <template #header>
          <div class="service">
            <!-- 标题移到ul上方 -->
            <div class="service-title">
              <el-text type="primary" size="large">客户服务</el-text>
            </div>
            <ul class="service_list">
              <li class="service_unit">
                <el-icon>
                  <Box />
                </el-icon>
                <el-text>品类齐全,轻松购物</el-text>
              </li>
              <li class="service_unit">
                <el-icon>
                  <House />
                </el-icon>
                <el-text>多仓直发，极速配送</el-text>
              </li>
              <li class="service_unit">
                <el-icon>
                  <Service />
                </el-icon>
                <el-text>正品行货，精致服务</el-text>
              </li>
              <li class="service_unit">
                <el-icon>
                  <Select />
                </el-icon>
                <el-text>天天低价，畅选无忧</el-text>
              </li>
            </ul>
          </div>
        </template>

        <div class="help_content">
          <!-- 购物指南 -->
          <div class="help_nav">
            <div class="help_nav_title">
              <el-text type="info" size="large">购物指南</el-text>
            </div>
            <div class="help_nav_list">
              <li>
                <a href="//help.jd.com/user/issue/list-29.html" target="_blank" rel="noopener noreferrer">
                  购物流程
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-30.html" target="_blank" rel="noopener noreferrer">
                  会员介绍
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-31.html" target="_blank" rel="noopener noreferrer">
                  生活旅行
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-32.html" target="_blank" rel="noopener noreferrer" class="highlight">
                  常见问题
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-33.html" target="_blank" rel="noopener noreferrer">
                  大家电
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-34.html" target="_blank" rel="noopener noreferrer">
                  联系客服
                </a>
              </li>
            </div>
          </div>

          <!-- 配送方式 -->
          <div class="help_nav">
            <div class="help_nav_title">
              <el-text type="info" size="large">配送方式</el-text>
            </div>
            <div class="help_nav_list">
              <li>
                <a href="//help.jd.com/user/issue/list-40.html" target="_blank" rel="noopener noreferrer">
                  上门自提
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-41.html" target="_blank" rel="noopener noreferrer">
                  211限时达
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-42.html" target="_blank" rel="noopener noreferrer">
                  配送服务查询
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-43.html" target="_blank" rel="noopener noreferrer">
                  配送收费标准
                </a>
              </li>
            </div>
          </div>

          <!-- 支付方式 -->
          <div class="help_nav">
            <div class="help_nav_title">
              <el-text type="info" size="large">支付方式</el-text>
            </div>
            <div class="help_nav_list">
              <li>
                <a href="//help.jd.com/user/issue/list-50.html" target="_blank" rel="noopener noreferrer">
                  货到付款
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-51.html" target="_blank" rel="noopener noreferrer">
                  在线支付
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-52.html" target="_blank" rel="noopener noreferrer">
                  分期付款
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-53.html" target="_blank" rel="noopener noreferrer">
                  公司转账
                </a>
              </li>
            </div>
          </div>

          <!-- 售后服务 -->
          <div class="help_nav">
            <div class="help_nav_title">
              <el-text type="info" size="large">售后服务</el-text>
            </div>
            <div class="help_nav_list">
              <li>
                <a href="//help.jd.com/user/issue/list-60.html" target="_blank" rel="noopener noreferrer">
                  售后政策
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-61.html" target="_blank" rel="noopener noreferrer">
                  价格保护
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-62.html" target="_blank" rel="noopener noreferrer">
                  退款说明
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-63.html" target="_blank" rel="noopener noreferrer">
                  返修/退换货
                </a>
              </li>
              <li>
                <a href="//help.jd.com/user/issue/list-64.html" target="_blank" rel="noopener noreferrer">
                  取消订单
                </a>
              </li>
            </div>
          </div>
        </div>


        <template #footer>
          <div class="copyright">© 2023 易购商城 版权所有</div>
        </template>
      </el-card>
    </div>
  </footer>
</template>

<style scoped lang="less">
.app-footer {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  background-color: #f5f5f5;
  padding: 20px 0;
}

.footer-content {
  width: 100%;
  max-width: 1650px;
  padding: 0 16px;
}

.service {
  width: 100%;
  padding: 15px 0;

  // 新增标题样式
  .service-title {
    margin-bottom: 15px;
    text-align: center;
  }

  .service_list {
    display: flex;
    justify-content: space-between; // 修改为space-between确保均匀分布
    align-items: center;
    width: 100%;
    padding: 0;
    margin: 0;
    list-style: none;
    height: 80px;
  }

  .service_unit {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    flex: 1; // 四个元素平分空间
    text-align: center;

    el-icon {
      font-size: 24px;
      color: #ff4d4f;
      margin-right: 8px;
    }

    el-text {
      font-size: 14px; // 调小字体
      color: #333;
      white-space: nowrap;
    }
  }
}

.help_content{
  padding: 20px 0;
  margin: 0 auto;
  width: 100%;
  overflow: hidden;
  justify-content: space-between;
  .help_nav{
    float: left;
    width: 25%; // 四个导航区域平分空间
    padding: 0 45px;
    box-sizing: border-box;
    justify-content: center;
    align-items: center;


    .help_nav_title{
      margin-bottom: 15px;
      font-size: 16px;
      font-weight: 500;
    }

    .help_nav_list{
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        margin-bottom: 10px;
      }

      a {
        color: #666;
        text-decoration: none;
        font-size: 12px; // 调小字体
        transition: color 0.3s;

        &:hover {
          color: #ff4d4f;
          text-decoration: underline;
        }

        &.highlight {
          color: #ff4d4f;
          font-weight: 500;
        }
      }
    }
  }
}

.copyright {
  font-size: 12px;
  color: #999;
  padding: 15px 0;
  text-align: center;
  margin-top: 10px;
}
</style>

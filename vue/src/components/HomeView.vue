<template>
  <el-container class="layout-container-demo" style="height: 100%">
    <!--侧边栏start-->
    <!--根据是否折叠属性，折叠时改变宽度-->
    <el-aside :style="isCollapse ? 'width:60px' : 'width:200px'">
      <el-scrollbar>
        <!--左侧上方header-->
        <div style="line-height: 60px; height: 60px">
          <el-image
            :src="require('../assets/icon/login.png')"
            style="float: left; width: 50px; height: 50px"
            fit="contain"
          ></el-image>
          <!--使用条件渲染v-if 折叠时隐藏文字-->
          <h5 v-if="!isCollapse" style="margin: 10px">图书管理系统</h5>
        </div>
        <!--菜单  isCollapse属性决定是否折叠-->
        <el-menu
          class="el-menu-vertical-demo"
          :default-openeds="[]"
          router
          :collapse="isCollapse"
        >
          <el-menu-item index="/dashboard"
            ><el-icon><HomeFilled /></el-icon
            ><template #title>Home</template></el-menu-item
          >

          <el-sub-menu index="basic" >
            <template #title>
                <el-icon>
                <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mingpian "></use>
        </svg></el-icon>
        <span> 个人信息</span>
            </template>
            <el-menu-item index="/person"
              ><el-link
                >
                <el-icon><svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-a-bianji1 "></use>
        </svg></el-icon> 修改信息</el-link
              ></el-menu-item
            >
            <el-menu-item index="/password"
              ><el-link
                ><el-icon><Notebook/></el-icon> 修改密码</el-link
              ></el-menu-item
            >
          </el-sub-menu>

          <el-menu-item index="/user" v-if="localUser.role == 1">
            <el-icon>
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconreader "></use>
            </svg></el-icon>
            <span>读者管理</span>
          </el-menu-item>
          <el-menu-item index="/book" v-if="localUser.role == 1">
            <el-icon>
              <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconbook "></use>
            </svg>  
            </el-icon>
            
            <span>书籍管理</span>
          </el-menu-item>
          <el-menu-item index="/book" v-if="localUser.role == 2">
            <el-icon><svg class="icon" aria-hidden="true">
              <use xlink:href="#iconbook "></use>
            </svg></el-icon>

            
            <span>图书查询</span>
          </el-menu-item>
          <el-menu-item index="/lendrecord" v-if="localUser.role == 1">
            <el-icon><svg class="icon" aria-hidden="true">
              <use xlink:href="#iconlend-record "></use>
            </svg>
            </el-icon>
            
            <span>借阅管理</span>
          </el-menu-item>
          <el-menu-item index="/lendrecord" v-if="localUser.role == 2">
            <el-icon><svg class="icon" aria-hidden="true">
              <use xlink:href="#iconlend-record"></use>
            </svg></el-icon>
            
            <span>借阅信息</span>
          </el-menu-item>
          <el-menu-item index="/bookwithuser">
            <el-icon><grid /></el-icon>
            <span>借阅状态</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <!--侧边栏end-->
    <el-container>
      <el-header>
        <!--折叠按钮 v-model绑定isCollapse属性，v-if条件渲染隐藏显示按钮-->
        <div style="float: left">
          <el-radio-group v-model="isCollapse">
            <el-radio-button :label="false" v-if="isCollapse"
              ><el-icon><Expand /></el-icon
            ></el-radio-button>
            <el-radio-button :label="true" v-if="!isCollapse"
              ><el-icon><Fold /></el-icon
            ></el-radio-button>
          </el-radio-group>
        </div>
        <!--使用Navigation 导航的Page Header 页头-->
        <el-page-header @back="$router.back()">
          <!-- <template #title>
                这是back的title插槽
              </template> -->
          <!-- <template #icon >
                这是back的Icon插槽
              </template>  -->
          <template #content>
            <!--content插槽放置面包屑导航-->
            <div>
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/dashboard' }">
                  Home
                </el-breadcrumb-item>
                <!--使用v-for列表渲染通过计算属性动态显示面包屑-->
                <el-breadcrumb-item
                  v-for="(item, index) in breadcrumbList"
                  :key="index"
                >
                  {{ item }}
                </el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </template>
          <!-- <template #breadcrumb>
                 <div class="myBreadcrumb">
                这是面包屑导航默认插槽，位于第一行
                </div>
              </template> -->
          <template #extra>
            <!--右侧扩展插槽，放置功能按钮，用户个人信息等-->
            <div>
              <el-button-group>
                <el-button
                  title="刷新"
                  style="border: none; font-size: 20px"
                  circle
                  plain
                  @click="refesh"
                >
                  <el-icon :size="20" style="vertical-align: middle">
                    <Refresh />
                  </el-icon>
                </el-button>
                <el-button
                  title="全屏"
                  style="border: none; font-size: 20px"
                  circle
                  plain
                  @click="fullScreen"
                >
                  <el-icon :size="14" style="vertical-align: middle">
                    <full-screen />
                  </el-icon>
                </el-button>
                <el-button
                  :title="dark ? '夜间模式' : '明亮模式'"
                  style="border: none; font-size: 20px"
                  circle
                  plain
                  @click="toggleTheme"
                >
                  <el-icon :size="14" style="vertical-align: middle">
                    <component :is="dark ? 'moon' : 'sunny'" />
                  </el-icon>
                </el-button>
              </el-button-group>

              <el-dropdown>
                <span>
                  你好!{{ localUser.username }}
                  <el-icon>
                    <arrow-down-bold />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="logout">
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
        </el-page-header>
      </el-header>

      <el-main>
        <el-scrollbar
          style="
            padding: 20px;
            box-sizing: border-box;
            background: #ccc;
            border-radius: 10px;
          "
        >
          <router-view />
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>
    <script>

import {ElMessage} from "element-plus";
export default {
  name: "HomeView",
  components: {
    // HelloWorld
  },
  data() {
    return {
      localUser:[],
      count: 100,
      content: "我的第一个VUE APP",
      username: "",
      isCollapse: false,
      dark: false,
    };
  },
  //计算属性，计算面包屑导航内容
  computed: {
    breadcrumbList() {
      let arr = [];
      let routePath = this.$route.name;
      console.log(routePath);
      if (routePath == "LendRecord") {
        arr = ["借阅信息"];
      } else if (routePath == "Person") {
        arr = ["个人信息"];
      }else if (routePath == "Password") {
        arr = ["修改密码"];
      } else if (routePath == "book") {
        arr = ["图书查询"];//只有图书查询正常
      } else if (routePath == "BookWithUser") {
        arr = ["借阅状态"];
    } else if (routePath == "User") {
        arr = ["读者管理"];
      }
      return arr;
    },
  },
  methods: {
    fullScreen() {
      if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen();
      } else if (document.exitFullscreen) {
        document.exitFullscreen();
      }
    },
    refesh() {
      location.reload();
    },
    toggleTheme() {
      this.dark = !this.dark;
      localStorage.setItem("dark", this.dark);
      document.documentElement.classList.toggle("dark", this.dark);
    },
    logout() {
      sessionStorage.clear();
      //Cookies.remove("username");
      //Cookies.remove("password");
      this.$router.push("/login");
      ElMessage.success("退出系统成功")
    },
  },
  created() {
    //this.username = sessionStorage.getItem("username");
    this.dark = eval(localStorage.getItem("dark"));
    document.documentElement.classList.toggle("dark", this.dark);
    let userStr = sessionStorage.getItem("user")||"{}"
    this.localUser = JSON.parse(userStr)
    console.log(this.localUser)
  },
  mounted() {
    //console.log(this.count);
    // this.$router.push("/main");
  },
};
</script>
    <style scoped>
.layout-container-demo .el-header {
  position: relative;
  /* background-color: var(--el-color-primary-light-7);
      color: var(--el-text-color-primary); */
  padding-top: 12px;
}
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);

  /* background: var(--el-color-primary-light-8);  */
}
.layout-container-demo .el-menu {
  border-right: none;
  background-color: "#545c64";
}
.layout-container-demo .el-main {
  padding: 0;
}
.layout-container-demo .toolbar {
  display: inline-flex;
  float: right;
  margin-right: 50px;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.el-dropdown {
  cursor: pointer;
  top: 5px;
}
</style>
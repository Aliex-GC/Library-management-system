<template>
   <!--<Header/>
     
  <div style="display: flex">
        
    <Aside/>
          内容区域-->
    
    <HomeView/>


</template>

<script>

//import Header from "../components/Header";
//import Aside from "../components/Aside";
import HomeView from "../components/HomeView";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Layout",
  components:{
    //Header,
    //Aside
    HomeView,
  }
}
</script>

<style scoped>

</style>
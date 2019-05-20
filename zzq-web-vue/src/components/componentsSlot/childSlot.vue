<template>    
  <div>
    <!--匿名插槽-->
    <slot></slot>
    <slot></slot>
    <!--具名插槽-->
    <slot name="test">
      child hello world
    </slot>
    <!--作用域插槽——带数据的插槽-->
    <slot :data="data"></slot>
    <slot :data="data"></slot>
  </div>
</template>

<script>
  export default {
    data: function(){
      return {
        data: ['zhangsan','lisi','wanwu','zhaoliu','tianqi','xiaoba']
      }
    }
  }
</script>

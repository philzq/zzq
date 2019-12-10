<template>
  <div>
    <parent-childslot>
      <div style="width: 100%;height: 100px;margin: auto auto;background-color: red;color: black">
        parent hello world1
      </div>
      <div slot="test" style="width: 100%;height: 100px;margin: auto auto;background-color: red;color: black">
        parent hello world2
      </div>
      <div style="width: 100%;height: 100px;margin: auto auto;background-color: red;color: black">
        parent hello world3
      </div>
      <div slot-scope="user" class="tmpl">
        <ul>
          <li v-for="item in user.data">{{item}}</li>
        </ul>
      </div>
    </parent-childslot>
  </div>
</template>

<script>
  import childslot from "./childSlot"

  export default {
    components: {
      "parent-childslot": childslot
    }
  }
</script>

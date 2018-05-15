<template>
  <div class="login">
    <div class="bac-div">
      <div class="login-content-main">
        <span class="login-content-title">高速路违法取证系统</span>
        <div class="login-form">
          <i-form ref="formInline"
                  :model="formInline"
                  :rules="ruleInline">
            <FormItem prop="userName">
              <i-input type="text"
                       v-model="formInline.userName"
                       placeholder="Username">
                <Icon type="ios-person-outline"
                      slot="prepend"></Icon>
              </i-input>
            </FormItem>
            <Form-item prop="password">
              <i-input type="password"
                       v-model="formInline.password"
                       placeholder="Password">
                <Icon type="ios-locked-outline"
                      slot="prepend"></Icon>
              </i-input>
            </Form-item>
            <Form-item>
              <i-button type="primary"
                        @click="handleSubmit('formInline')">登录</i-button>
            </Form-item>
          </i-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ajax from '@/util/ajax'

export default {
  name: 'login',
  data: function () {
    return {
      formInline: {
        userName: '',
        password: ''
      },
      ruleInline: {
        userName: [
          { required: true, message: '请填写用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请填写密码', trigger: 'blur' },
          { type: 'string', min: 6, max: 12, message: '密码长度不能小于6位,不能大于12', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit (name) {
      let that = this
      this.$refs[name].validate((valid) => {
        if (valid) {
          let param = new URLSearchParams()
          for (let i in that.formInline) {
            param.append(i, that.formInline[i])
          }
          ajax.post({
            url: 'api/hifs/user/checkUser',
            param: param,
            success: res => {
              if (res.data.flag === true) {
                this.$Message.success('提交成功!')
                localStorage.setItem('user', res.data.user.id)
                this.$router.push('/')
              } else {
                this.$Message.error(res.data.error)
              }
            }
          })
        } else {
          this.$Message.error('表单验证失败!')
        }
      })
    }
  }
}
</script>

<style>
.login {
    width: 100%;
    height: 100%;
    background: url('../../assets/img/loginBck.jpg');
    background-size: 100% 100%;
}

.bac-div {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 999;
    background: #333;
    filter: alpha(opacity=60);
    -moz-opacity: 0.6;
    -khtml-opacity: 0.6;
    opacity: 0.6;
}

.login-content-main {
    width: 500px;
    height: 500px;
    margin: 0px auto;
    margin-top: -250px;
    position: relative;
    top: 50%;
    text-align: center;
    padding: 50px 20px;
    color: white;
}

.login-content-title {
    font-size: 40px;
}

.login-form {
    width: 70%;
    margin: 80px auto;
}
</style>

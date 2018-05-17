import axios from 'axios'

export default {
  get (data) {
    axios
      .get(data.url, data.param ? data.param : {})
      .then(data.success)
      .catch(response => {
        console.log(response)
      })
  },
  post (data) {
    axios
      .post(data.url, data.param ? data.param : {}, data.config ? data.config : {})
      .then(data.success)
      .catch(response => {
        console.log(response)
      })
  }
}

angular.module('app.cardprint', []).controller('cardprintCtrl', function($scope) {
})

.service('HttpService', function($http) {
  return {
    getPost: function() {
      // $http returns a promise, which has a then function, which also returns a promise.
      return $http.get('http://jsonplaceholder.typicode.com/posts/1')
       .then(function (response) {
       // In the response, resp.data contains the result. Check the console to see all of the data returned.
       console.log('Get Post', response);
       return response.data;
      });
    }
  };
});
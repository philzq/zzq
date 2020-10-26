/*--------------------------------------------------------------
 | 微信管理-服务号录入 
 +--------------------------------------------------------------
 | Author: chensenrong
 +--------------------------------------------------------------
 | Date: 2019-12-04
 +--------------------------------------------------------------
*/
(function () {
    /**
     * 全局变量
     */
    var $ctrl = this;
    var refreshTable = null; // 用于刷新表格全局函数
    var app = angular.module('CourseTagControllerApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.selection', 'ui.grid.autoResize', 'ui.grid.pagination']);
    /**
     * 列表控制器
     */
    app.controller('CourseTagController', ['$scope', 'i18nService', 'uiGridConstants', '$uibModal', 'CourseTagService', function ($scope, i18nService, uiGridConstants, $modal, service) {
        // 分页初始化设置
        var pageOptions = {
            page: 1
        }

        // 分页参数
        var pageParams = {
            sort: ''
        };

        $scope.queryForm = {};

        //查询需要的参数
        $scope.queryFormInit = function(){
            $scope.queryForm = {};
            refreshTable();
        };

        // 显示、隐藏按钮
        $scope.showOrHide = '隐藏过滤';

        // 表格语言中文
        i18nService.setCurrentLang('zh-cn');

        $scope.gridOptions = {
            paginationPageSizes: [5, 10, 25, 50, 75, 100, 500],
            paginationPageSize: 25,
            useExternalPagination: true,
            useExternalSorting: false,
            enableGridMenu: true,
            enableSorting: false,
            enableFiltering: true,
            useExternalFiltering: true,
            enableRowSelection: false,
            enableRowHeaderSelection: false,
            enableColumnResizing: true,
            columnDefs: [
                {
                    field: 'id',
                    name: 'ID',
                    minWidth: 100,
                    enableSorting: false,
                    enableFiltering: false,
                    cellTemplate: function () {
                        return '<div class="el-line-height-30" title="{{row.entity.id}}">{{row.entity.id}}</div>';
                    }
                },
                {
                    field: 'name',
                    name: '标签名称',
                    displayName: '标签名称',
                    minWidth: 100,
                    enableSorting: false,
                    enableFiltering: false,
                    cellTemplate: function () {
                        return '<div class="el-line-height-30" title="{{row.entity.name}}">{{row.entity.name}}</div>';
                    }
                },
                {
                    field: 'weight',
                    name: '权重',
                    minWidth: 100,
                    enableSorting: false,
                    enableFiltering: false,
                    cellTemplate: function () {
                        return '<div class="el-line-height-30" title="{{row.entity.weight}}">{{row.entity.weight}}</div>';
                    }
                },
                {
                    field: 'action',
                    name: '操作',
                    cellClass: 'controller-action-btn',
                    width: 100,
                    enableSorting: false,
                    enableFiltering: false,
                    cellTemplate: function () {
                        return '<div class="el-line-height-30">' +
                            '<a has-perm="COURSE_TAG_SAVE" data-toggle="tooltip" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket el-mg-r-5" ng-click="grid.appScope.openEditWin(row.entity)"><i class="fa fa-fw fa-edit"></i></a>' +
                            '<a has-perm="COURSE_TAG_SAVE" data-toggle="tooltip" title="删除" class="btn btn-social-icon btn-xs btn-danger el-mg-l-5" ng-click="grid.appScope.openDelWin(row.entity.id)"><i class="fa fa-fw fa-remove"></i></a>' +
                            '</div>';
                    }
                }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;

                /**
                 * 分页
                 */
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                    pageParams.pageNumber = newPage;
                    pageParams.pageSize = pageSize;

                    // 请求更新表格数据
                    refreshTable();
                });
            }
        }

        /**
         * 请求表格全局函数
         * @return viod
         */
        refreshTable = function () {
            clearInterval(utils.time);

            utils.time = setTimeout(function () {
                pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage === undefined ? pageOptions.page : $scope.gridOptions.paginationCurrentPage;
                pageParams.pageSize = $scope.gridOptions.paginationPageSize;
                let params = Object.assign({},pageParams, $scope.queryForm);
                service.getPage(params, function (response) {
                    if (response.code == utils.code.success) {
                        $scope.gridOptions.totalItems = response.resultData.totalElements;
                        $scope.gridOptions.data = response.resultData.content;
                    }
                });
            }, utils.timeout);
        };

        refreshTable();

        $scope.refreshTable = refreshTable;

        /**
         * 新增/编辑
         */
        $scope.openAddWin = function () {
            $modal.open({
                animation: true,
                templateUrl: 'course/tag/save.html',
                controller: 'CourseTagSaveController',
                resolve: {
                    items: {}
                }
            });
        }

        /**
         * 编辑
         */
        $scope.openEditWin = function (entity) {
            // 删除黑色泡泡提示
            utils.deletTip();

            $ctrl.items = {
                id : entity.id,
                name : entity.name,
                weight : entity.weight
            };

            $modal.open({
                animation: true,
                templateUrl: 'course/tag/save.html',
                controller: 'CourseTagSaveController',
                controllerAs: '$ctrl',
                resolve: {
                    items: function () {
                        return $ctrl.items;
                    }
                }
            });
        }

        /**
         * 删除
         */
        $scope.openDelWin = function (id) {
            // 删除黑色泡泡提示
            utils.deletTip();

            $ctrl.items = {
                id: id,
                deleted: 1
            }

            $modal.open({
                animation: true,
                templateUrl: 'course/tag/confirm.html',
                controller: 'CourseTagSaveController',
                controllerAs: '$ctrl',
                resolve: {
                    items: function () {
                        return $ctrl.items;
                    }
                }
            });
        }
    }]);

    /**
     * 新增/编辑控制器
     */
    app.controller('CourseTagSaveController', ['$scope', '$uibModalInstance', 'items', 'CourseTagService', function ($scope, $uibModalInstance, items, service) {
        // 初始化
        $scope.loading = true;
        $scope.items = {};
        Object.assign($scope.items, items);
        /**
         * 取消弹窗
         */
        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
        //删除需要的一些数据--软删除
        // 初始化表单默认选择变量
        $scope.loading = true;
        $scope.title = '删除';
        $scope.buttonName = '确定';
        $scope.content = '确定删除该课程标签吗？';

        /**
         * 提交表单
         */
        $scope.save = function () {
            if ($scope.loading) {
                // 禁止按钮
                $scope.myForm.$invalid = true;
                $scope.loading = false;

                service.save($scope.items, function (response) {
                    if (response.code === utils.code.success) {
                        toastr['success']('新增/编辑成功');
                        $scope.cancel();
                        refreshTable();
                    } else {
                        $scope.myForm.$invalid = false;
                        $scope.loading = true;
                    }
                });
            }
        };

    }]);

    /**
     * 自定义请求服务
     */
    app.factory('CourseTagService', ['$http', function ($http) {
        return {
            /**
             * 获取表格分页数据
             * @param {object} pageParams 页码、条数、过滤、排序
             * @param {function} callbackFun 分页回调
             * @return void
             */
            getPage: function (pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

                $http.get('/course/tag/page', {
                    params: pageParams
                })
                    .success(function (response) {
                        if (callbackFun) {
                            callbackFun(response);
                        } else {
                            throw new Error('未填写参数');
                        }
                    });
            },
            /**
             * 新增/编辑
             */
            save: function (params, callbackFun) {
                var formData = new FormData();

                for (var i in params) {
                    formData.append(i, params[i]);
                }

                $http({
                    method: 'post',
                    url: '/course/tag/save',
                    data: formData,
                    headers: {
                        'Content-Type': undefined
                    },
                    transformRequest: angular.identity
                }).success(function (response) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else {
                        throw new Error('未填写参数');
                    }
                }).error(function (response) {
                    callbackFun(response);
                });
            },
        }
    }]);
})();
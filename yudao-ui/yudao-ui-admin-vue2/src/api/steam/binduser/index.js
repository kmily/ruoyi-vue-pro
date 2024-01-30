import request from '@/utils/request'

// 创建 steam用户绑定
export function createBindUser(data) {
  return request({
    url: '/steam/bind-user/create',
    method: 'post',
    data: data
  })
}

// 更新 steam用户绑定
export function updateBindUser(data) {
  return request({
    url: '/steam/bind-user/update',
    method: 'put',
    data: data
  })
}

// 删除 steam用户绑定
export function deleteBindUser(id) {
  return request({
    url: '/steam/bind-user/delete?id=' + id,
    method: 'delete'
  })
}

// 获得 steam用户绑定
export function getBindUser(id) {
  return request({
    url: '/steam/bind-user/get?id=' + id,
    method: 'get'
  })
}

// 获得 steam用户绑定分页
export function getBindUserPage(params) {
  return request({
    url: '/steam/bind-user/page',
    method: 'get',
    params
  })
}
// 导出 steam用户绑定 Excel
export function exportBindUserExcel(params) {
  return request({
    url: '/steam/bind-user/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
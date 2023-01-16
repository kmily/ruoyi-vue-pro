import request from '@/utils/request'

// 创建扫描用户
export function createUsers(data) {
  return request({
    url: '/scan/users/create',
    method: 'post',
    data: data
  })
}

// 更新扫描用户
export function updateUsers(data) {
  return request({
    url: '/scan/users/update',
    method: 'put',
    data: data
  })
}

// 删除扫描用户
export function deleteUsers(id) {
  return request({
    url: '/scan/users/delete?id=' + id,
    method: 'delete'
  })
}

// 获得扫描用户
export function getUsers(id) {
  return request({
    url: '/scan/users/get?id=' + id,
    method: 'get'
  })
}

// 获得扫描用户分页
export function getUsersPage(query) {
  return request({
    url: '/scan/users/page',
    method: 'get',
    params: query
  })
}

// 导出扫描用户 Excel
export function exportUsersExcel(query) {
  return request({
    url: '/scan/users/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

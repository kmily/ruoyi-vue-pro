import request from '@/utils/request'

// 创建用户组
export function createGroup(data) {
  return request({
    url: '/system/group/create',
    method: 'post',
    data: data
  })
}

// 更新用户组
export function updateGroup(data) {
  return request({
    url: '/system/group/update',
    method: 'put',
    data: data
  })
}

// 删除用户组
export function deleteGroup(id) {
  return request({
    url: '/system/group/delete?id=' + id,
    method: 'delete'
  })
}

// 获得用户组
export function getGroup(id) {
  return request({
    url: '/system/group/get?id=' + id,
    method: 'get'
  })
}

// 获得用户组分页
export function getGroupPage(query) {
  return request({
    url: '/system/group/page',
    method: 'get',
    params: query
  })
}

// 导出用户组 Excel
export function exportGroupExcel(query) {
  return request({
    url: '/system/group/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

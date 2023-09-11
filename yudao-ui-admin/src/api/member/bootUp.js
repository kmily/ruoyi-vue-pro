import request from '@/utils/request'

// 创建用户启动数据
export function createBootUp(data) {
  return request({
    url: '/member/boot-up/create',
    method: 'post',
    data: data
  })
}

// 更新用户启动数据
export function updateBootUp(data) {
  return request({
    url: '/member/boot-up/update',
    method: 'put',
    data: data
  })
}

// 删除用户启动数据
export function deleteBootUp(id) {
  return request({
    url: '/member/boot-up/delete?id=' + id,
    method: 'delete'
  })
}

// 获得用户启动数据
export function getBootUp(query) {
  return request({
    url: '/member/boot-up/get',
    method: 'get',
    params: query
  })
}

// 获得用户启动数据分页
export function getBootUpPage(query) {
  return request({
    url: '/member/boot-up/page',
    method: 'get',
    params: query
  })
}

// 导出用户启动数据 Excel
export function exportBootUpExcel(query) {
  return request({
    url: '/member/boot-up/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

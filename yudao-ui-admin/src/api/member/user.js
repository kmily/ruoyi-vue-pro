import request from '@/utils/request'

// 创建会员
export function createUser(data) {
  return request({
    url: '/member/user/create',
    method: 'post',
    data: data
  })
}

// 更新会员
export function updateUser(data) {
  return request({
    url: '/member/user/update',
    method: 'put',
    data: data
  })
}

// 删除会员
export function deleteUser(id) {
  return request({
    url: '/member/user/delete?id=' + id,
    method: 'delete'
  })
}

// 获得会员
export function getUser(id) {
  return request({
    url: '/member/user/get?id=' + id,
    method: 'get'
  })
}

// 获得会员分页
export function getUserPage(query) {
  return request({
    url: '/member/user/page',
    method: 'get',
    params: query
  })
}

// 导出会员 Excel
export function exportUserExcel(query) {
  return request({
    url: '/member/user/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 根据手机号查询用户
export function getUserByMobile(mobile){
  return request({
    url: '/member/user/mobile?mobile=' + mobile,
    method: 'get',
  })
}

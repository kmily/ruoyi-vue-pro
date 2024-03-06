import request from '@/utils/request'

// 创建用户查询明细
export function createYouyouDetails(data) {
  return request({
    url: '/steam/youyou-details/create',
    method: 'post',
    data: data
  })
}

// 更新用户查询明细
export function updateYouyouDetails(data) {
  return request({
    url: '/steam/youyou-details/update',
    method: 'put',
    data: data
  })
}

// 删除用户查询明细
export function deleteYouyouDetails(id) {
  return request({
    url: '/steam/youyou-details/delete?id=' + id,
    method: 'delete'
  })
}

// 获得用户查询明细
export function getYouyouDetails(id) {
  return request({
    url: '/steam/youyou-details/get?id=' + id,
    method: 'get'
  })
}

// 获得用户查询明细分页
export function getYouyouDetailsPage(params) {
  return request({
    url: '/steam/youyou-details/page',
    method: 'get',
    params
  })
}
// 导出用户查询明细 Excel
export function exportYouyouDetailsExcel(params) {
  return request({
    url: '/steam/youyou-details/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
import request from '@/utils/request'

// 创建页面访问数据
export function createVisitPage(data) {
  return request({
    url: '/member/visit-page/create',
    method: 'post',
    data: data
  })
}

// 更新页面访问数据
export function updateVisitPage(data) {
  return request({
    url: '/member/visit-page/update',
    method: 'put',
    data: data
  })
}

// 删除页面访问数据
export function deleteVisitPage(id) {
  return request({
    url: '/member/visit-page/delete?id=' + id,
    method: 'delete'
  })
}

// 获得页面访问数据
export function getVisitPage(id) {
  return request({
    url: '/member/visit-page/get?id=' + id,
    method: 'get'
  })
}

// 获得页面访问数据分页
export function getVisitPagePage(query) {
  return request({
    url: '/member/visit-page/list',
    method: 'get',
    params: query
  })
}

// 导出页面访问数据 Excel
export function exportVisitPageExcel(query) {
  return request({
    url: '/member/visit-page/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

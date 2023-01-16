import request from '@/utils/request'

// 创建体型解决方案
export function createShapeSolution(data) {
  return request({
    url: '/scan/shape-solution/create',
    method: 'post',
    data: data
  })
}

// 更新体型解决方案
export function updateShapeSolution(data) {
  return request({
    url: '/scan/shape-solution/update',
    method: 'put',
    data: data
  })
}

// 删除体型解决方案
export function deleteShapeSolution(id) {
  return request({
    url: '/scan/shape-solution/delete?id=' + id,
    method: 'delete'
  })
}

// 获得体型解决方案
export function getShapeSolution(id) {
  return request({
    url: '/scan/shape-solution/get?id=' + id,
    method: 'get'
  })
}

// 获得体型解决方案分页
export function getShapeSolutionPage(query) {
  return request({
    url: '/scan/shape-solution/page',
    method: 'get',
    params: query
  })
}

// 导出体型解决方案 Excel
export function exportShapeSolutionExcel(query) {
  return request({
    url: '/scan/shape-solution/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

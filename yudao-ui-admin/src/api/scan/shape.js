import request from '@/utils/request'

// 创建体型
export function createShape(data) {
  return request({
    url: '/scan/shape/create',
    method: 'post',
    data: data
  })
}

// 更新体型
export function updateShape(data) {
  return request({
    url: '/scan/shape/update',
    method: 'put',
    data: data
  })
}

// 删除体型
export function deleteShape(id) {
  return request({
    url: '/scan/shape/delete?id=' + id,
    method: 'delete'
  })
}

// 获得体型
export function getShape(id) {
  return request({
    url: '/scan/shape/get?id=' + id,
    method: 'get'
  })
}

// 获得体型分页
export function getShapePage(query) {
  return request({
    url: '/scan/shape/page',
    method: 'get',
    params: query
  })
}

// 导出体型 Excel
export function exportShapeExcel(query) {
  return request({
    url: '/scan/shape/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

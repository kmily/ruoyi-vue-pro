import request from '@/utils/request'

// 创建行政区域
export function createArea(data) {
  return request({
    url: '/system/area/create',
    method: 'post',
    data: data
  })
}

// 更新行政区域
export function updateArea(data) {
  return request({
    url: '/system/area/update',
    method: 'put',
    data: data
  })
}

// 删除行政区域
export function deleteArea(id) {
  return request({
    url: '/system/area/delete?id=' + id,
    method: 'delete'
  })
}

// 获得行政区域
export function getArea(id) {
  return request({
    url: '/system/area/get?id=' + id,
    method: 'get'
  })
}

// 获得行政区域分页
export function getAreaPage(query) {
  return request({
    url: '/system/area/page',
    method: 'get',
    params: query
  })
}


// 导出行政区域 Excel
export function exportAreaExcel(query) {
  return request({
    url: '/system/area/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 获得全部省
export function provinces(data) {
  return request({
    url: '/system/area/provinces',
    method: 'post',
    data: data
  })
}

// 获得全部市
export function cities(data) {
  return request({
    url: '/system/area/cities',
    method: 'post',
    data: data
  })
}

// 获得行政区域
export function counties(data) {
  return request({
    url: '/system/area/counties',
    method: 'post',
    data: data
  })
}

// 获得行政区域
export function towns(data) {
  return request({
    url: '/system/area/towns',
    method: 'post',
    data: data
  })
}

// 获得行政区域
export function villages(data) {
  return request({
    url: '/system/area/villages',
    method: 'post',
    data: data
  })
}


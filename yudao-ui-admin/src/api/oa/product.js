import request from '@/utils/request'

// 创建产品
export function createProduct(data) {
  return request({
    url: '/oa/product/create',
    method: 'post',
    data: data
  })
}

// 更新产品
export function updateProduct(data) {
  return request({
    url: '/oa/product/update',
    method: 'put',
    data: data
  })
}

// 删除产品
export function deleteProduct(id) {
  return request({
    url: '/oa/product/delete?id=' + id,
    method: 'delete'
  })
}

// 获得产品
export function getProduct(id) {
  return request({
    url: '/oa/product/get?id=' + id,
    method: 'get'
  })
}

// 获得产品分页
export function getProductPage(query) {
  return request({
    url: '/oa/product/page',
    method: 'get',
    params: query
  })
}

// 导出产品 Excel
export function exportProductExcel(query) {
  return request({
    url: '/oa/product/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

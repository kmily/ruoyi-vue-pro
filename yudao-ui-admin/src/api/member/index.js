import request from '@/utils/request'

// 创建会员
export function selectCount() {
  return request({
    url: '/member/index/get',
    method: 'get',
  })
}